# morse

Decode ambiguous Morse codes using dictionaries.

## Why?

I'm an owner of a beautiful travel mug made by a Russian manufacturer `Biostal` for couple of years already. Recently, I found out that the logo on this mug has a set of dashes and dots, which looks like a Morse code `-.-..-.---......----.---.`:

![mug's logo](doc/mug.jpg)

I wanted to check if this code-like dashed line is actually a meaningful message encoded using Morse code or just a random sequence of graphical primitives. As you may know, crutial part of Morse code is pauses being made between letters and words, but apparently a sequence on the logo was missing this information, which makes decoding indeterminate. For instance, `-.` code without knowing where letter- and word-breaks should be, may equaly be decoded as `et` or `a`.

## How does it work?

I anticipated that taking bruteforce approach of just finding all possible input string permutations resuling in a given code would produce too many non-sence options, so that I'll be flooded with them and won't find original message (if there was any). I actually wrote `messages-amount` function, which finds the amount of all possible input messages for a given code. It was 6994659 for the code from my mug. No way I will try to go through them manually.

So I decided to limit possible input messages to only those, which may have a chance of making sense by composing them from acutall natural language words, which, in turn, my lib takes from dictionaries. Even this approach was giving me tons of stuff like `tln md i ut matte`. Finnaly, I relized that within a taken approach, the message with the least amount of word would be the most meaningful. That's why current lib sorts all found input messages by the amount of words in ascending order and returns first 50 items.

## Did I manage to decode my travel mug?

Yes. Apparently code says: `crosstown`, which is a name of a product line my mug belongs to. I also tried to decode it using Russian words and Russian Morse code, but the most meaningful message I got is `куем несем тын`, which, well is not tremendously likely was the orignial message.

## Where did I get dictionaries from?

* English: https://github.com/dwyl/english-words
* Russian: https://github.com/danakt/russian-words
